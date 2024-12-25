package com.metacoding.web_project.transaction;

import com.metacoding.web_project._core.util.FormatDate;
import com.metacoding.web_project._core.util.PageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;


@Import(TransactionRepository.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    // 낙찰된 물품(판매) 목록 조회 (판매 확정 안 누른 상태) 테스트
    @Test
    public void findBySellerIdNotConfirmOfSell_test() {
        // given (테스트를 위한 입력값)
        Integer id = 1;

        // when (테스트할 메서드 실행)
        List<Transaction> transactionList = transactionRepository.findBySellerIdNotConfirmOfSell(id, PageUtil.offsetCount("1", 5), 5);

        // then(eye) (결과 검증 / 출력으로 직접 확인은 eye)
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getId());
            System.out.println(transaction.getGoods().getTitle());
            System.out.println(transaction.getGoods().getCategory().getName());
            System.out.println(transaction.getGoods().getImgUrl());
            System.out.println(FormatDate.formatToyyyypMMpdd(transaction.getUpdatedAt()));
            System.out.println(transaction.getSuccessPrice());
            System.out.println(transaction.getSellerStatus());
            System.out.println(transaction.getBuyer().getAddr());
            System.out.println("=======================");
        }
    }

    // 낙찰된 물품(구매) 목록 DTO(구매 확정 누름, 안누름 전부 포함) 테스트
    @Test
    public void findByBuyerIdForAllBuy_test() {
        // given (테스트를 위한 입력값)
        Integer id = 1;

        // when (테스트할 메서드 실행)
        List<Transaction> transactionList = transactionRepository.findByBuyerIdForBuy(id, PageUtil.offsetCount("1", 3), 3);

        // then(eye) (결과 검증 / 출력으로 직접 확인은 eye)
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getId());
            System.out.println(transaction.getGoods().getTitle());
            System.out.println(transaction.getGoods().getSeller().getName());
            System.out.println(transaction.getGoods().getCategory().getName());
            System.out.println(transaction.getGoods().getImgUrl());
            System.out.println(transaction.getSuccessPrice());
            System.out.println(transaction.getDeliveryNum());
            System.out.println(transaction.getBuyerStatus());
            System.out.println("=======================");
        }
    }
}