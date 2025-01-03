package com.metacoding.web_project.transaction;

import com.metacoding.web_project._core.util.PageUtil;
import com.metacoding.web_project._core.error.ex.Exception404;
import com.metacoding.web_project.bid.Bid;
import com.metacoding.web_project.bid.BidRepository;
import com.metacoding.web_project.goods.Goods;
import com.metacoding.web_project.user.User;
import com.metacoding.web_project.user.UserRepository;
import com.metacoding.web_project.useraccount.UserAccount;
import com.metacoding.web_project.useraccount.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BidRepository bidRepository;
    private final UserAccountRepository userAccountRepository;

    private final UserRepository userRepository;

    @Transactional
    public void save(TransactionRequest.SaveDTO saveDTO) {
        Integer goodsId = saveDTO.getGoodsId();
        Bid bid = bidRepository.findByIdBidDESC(goodsId);
        Integer tryPrice = bid.getTryPrice();
        Goods goods = bid.getGoods();
        User buyer = bid.getBuyer();
        User seller = goods.getSeller();
        transactionRepository.save(saveDTO.toEntity(goods, buyer, seller, tryPrice));
    }

    // 조건에 따라 최대 10개의 transaction 행을 유저 정보와 함께 가져오는 메서드 (관리자)
    public List<TransactionResponse.TransactionDTO> findTransactionTBAndUser(String divide, String search, String page) {
        String query;

        // divide에 따라 조건문 생성
        if (divide.equals("buyer")) {
            query = "where t.buyer.name like '%" + search + "%'";
        } else if (divide.equals("seller")) {
            query = "where t.seller.name like '%" + search + "%'";
        } else {
            query = "where t.goods.title like '%" + search + "%'";
        }

        // 쿼리 실행 및 결과 반환
        List<Transaction> transactionList = transactionRepository.findTransactionJoinAnotherInfo(query, PageUtil.offsetCount(page, 10), 10);
        List<TransactionResponse.TransactionDTO> dtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            dtoList.add(new TransactionResponse.TransactionDTO(transaction));
        }
        return dtoList;
    }

    // 조건에 맞는 transaction 테이블 행의 총 개수를 구하는 메서드
    public Integer findTransactionsCount(String divide, String search) {
        String query;
        // divide에 따라 조건문 생성
        if (divide.equals("buyer")) {
            query = " where t.buyer.name like '%" + search + "%'";
        } else if (divide.equals("seller")) {
            query = " where t.seller.name like '%" + search + "%'";
        } else {
            query = " where t.goods.title like '%" + search + "%'";
        }
        return transactionRepository.findTransactionsCount(query);
    }

    // 낙찰된 물품(판매) 화면 열기 - 판매 확정 누름, 안 누름 포함
    @Transactional
    public List<TransactionResponse.CompleteAuctionDTO> completeAuctionList(Integer userId, String page) {
        
        // 임시로 sellerId = 1인 경우만 가져옴, 로그인과 연결할 때 바꿀 것
        List<Transaction> transactionList = transactionRepository.findBySellerIdNotConfirmOfSell(userId, PageUtil.offsetCount(page, 5), 5);

        // completeAuctionDTO로 변환
        List<TransactionResponse.CompleteAuctionDTO> completeAuctionDTOList = new ArrayList<>();

        for (Transaction transaction : transactionList) {
            completeAuctionDTOList.add(new TransactionResponse.CompleteAuctionDTO(transaction));
        }
        return completeAuctionDTOList;
    }

    // 낙찰된 물품(판매) 화면 - 송장번호등록(transaction_tb 테이블의 delivery_num update)
    @Transactional
    public void updateDeliveryNumber(TransactionRequest.UpdateDeliveryNumberDTO updateDeliveryNumberDTO) {
        Transaction transaction = transactionRepository.findById(updateDeliveryNumberDTO.getTransactionId())
                .orElseThrow(() -> new Exception404("해당 물품이 없습니다."));

        transaction.updateStatus(null, null, null, updateDeliveryNumberDTO.getDeliveryNumber());
    }

    // 낙찰된 물품(판매) 화면 - 판매 확정하기(transaction_tb 테이블의 seller_status = 1로 update)
    @Transactional
    public void updateSellerStatus(TransactionRequest.UpdateSellerStatusDTO updateSellerStatusDTO) {
        Transaction transaction = transactionRepository.findById(updateSellerStatusDTO.getTransactionId())
                .orElseThrow(() -> new Exception404("해당 물품이 없습니다."));

        transaction.updateStatus(null, updateSellerStatusDTO.getSellerStatus(), null, null);

        // 판매 확정 + 구매 확정된 물품의 판매자의 돈에 낙찰가 추가
        if (transaction.getSellerStatus() == 1 && transaction.getBuyerStatus() == 1) {
            UserAccount userAccountSeller = userAccountRepository.findById(transaction.getSeller().getId());
            UserAccount userAccountBuyer = userAccountRepository.findById(transaction.getBuyer().getId());
            userAccountSeller.updateUserInfo(transaction.getSuccessPrice());
            userAccountSeller.updateUserScore(5);
            userAccountBuyer.updateUserScore(5);
        }
    }

    // 낙찰된 물품(판매) 화면 - 판매 취소하기(transaction_tb 테이블의 transaction_status = 1로 update)
    @Transactional
    public void updateTransactionStatusForSeller(TransactionRequest.UpdateTransactionStatusForSellerDTO updateTransactionStatusForSellerDTO) {
        Transaction transaction = transactionRepository.findById(updateTransactionStatusForSellerDTO.getTransactionId())
                .orElseThrow(() -> new Exception404("해당 물품이 없습니다."));

        transaction.updateStatus(null, null, updateTransactionStatusForSellerDTO.getTransactionStatus(), null);

        // 판매 취소 됐을 때 해당 구매자의 돈 반환
        UserAccount userAccountBuyer = userAccountRepository.findById(transaction.getBuyer().getId());
        userAccountBuyer.updateUserInfo(transaction.getSuccessPrice());

        UserAccount userAccountSeller = userAccountRepository.findById(transaction.getSeller().getId());
        userAccountSeller.updateUserScore(-5);
    }

    // 낙찰된 물품(구매) 화면 열기 - 구매 확정 누름, 안 누름 다 포함
    @Transactional
    public List<TransactionResponse.ParticipatedAuctionDTO> participatedAuctionList(Integer userId, String page) {

        // 임시로 buyerId = 1인 경우만 가져옴, 로그인과 연결할 때 바꿀 것
        List<Transaction> transactionList = transactionRepository.findByBuyerIdForBuy(userId, PageUtil.offsetCount(page, 5), 5);

        // ParticipatedAuctionDTO로 변환
        List<TransactionResponse.ParticipatedAuctionDTO> participatedAuctionDTOList = new ArrayList<>();

        for (Transaction transaction : transactionList) {
            participatedAuctionDTOList.add(new TransactionResponse.ParticipatedAuctionDTO(transaction));
        }
        return participatedAuctionDTOList;
    }

    // 낙찰된 물품(구매) 화면에서 필요한 총 요소 count
    public int participatedAuctionListCount(Integer userId) {
        return transactionRepository.findByBuyerIdForAllBuyCount(userId);
    }

    // 낙찰된 물품(구매) 화면 - 구매 확정하기(transaction_tb 테이블의 buyer_status = 1로 update)
    @Transactional
    public void updateBuyerStatus(TransactionRequest.UpdateBuyerStatusDTO updateBuyerStatusDTO) {
        Transaction transaction = transactionRepository.findById(updateBuyerStatusDTO.getTransactionId())
                .orElseThrow(() -> new Exception404("해당 물품이 없습니다."));


        transaction.updateStatus(updateBuyerStatusDTO.getBuyerStatus(), null, null, null);
        // 판매 확정 + 구매 확정된 물품의 판매자의 돈에 낙찰가 추가
        if (transaction.getSellerStatus() == 1 && transaction.getBuyerStatus() == 1) {
            UserAccount userAccountSeller = userAccountRepository.findById(transaction.getSeller().getId());
            UserAccount userAccountBuyer = userAccountRepository.findById(transaction.getBuyer().getId());
            userAccountSeller.updateUserInfo(transaction.getSuccessPrice());
            userAccountSeller.updateUserScore(5);
            userAccountBuyer.updateUserScore(5);
        }
    }

    // 낙찰된 물품(구매) 화면 - 구매 취소하기(transaction_tb 테이블의 transaction_status = 1로 update)
    @Transactional
    public void updateTransactionStatusForBuyer(TransactionRequest.UpdateTransactionStatusForBuyerDTO updateTransactionStatusForBuyerDTO) {
        Transaction transaction = transactionRepository.findById(updateTransactionStatusForBuyerDTO.getTransactionId())
                .orElseThrow(() -> new Exception404("해당 물품이 없습니다."));

        transaction.updateStatus(null, null, updateTransactionStatusForBuyerDTO.getTransactionStatus(), null);

        // 구매 취소 됐을 때 해당 구매자의 돈 반환
        UserAccount userAccount = userAccountRepository.findById(transaction.getBuyer().getId());
        userAccount.updateUserInfo(transaction.getSuccessPrice());
        userAccount.updateUserScore(-5);
    }

    // 낙찰된 물품 (판매) 페이지에 필요한 List의 총 개수 반환
    public Integer totalCompleteAuctionListCount(Integer userId) {
        return transactionRepository.findBySellerIdNotConfirmOfSellCount(userId);
    }
}