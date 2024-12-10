package com.metacoding.web_project.bid;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BidRepository {
    private final EntityManager em;

    // 상세페이지 현재 최고 경매가 데이터 추출
    public Optional<Bid> findByGoodsDesc(Integer id) {
        Query q = em.createNativeQuery("select * from bid_tb where goods_id = ? order by id desc ", Bid.class);
        q.setParameter(1, id);
        return Optional.ofNullable((Bid) q.getSingleResult());
    }

<<<<<<< HEAD
    public void saveV1(Bid bid) {
        em.persist(bid);
=======
    // bid 테이블을 join하여 조회(goods,user)(관리자용)
    public List<Bid> findAllBidsJoinAnotherInfo(String condition) {
        String sql = """
                select b from Bid b join fetch b.buyer join fetch b.goods g join fetch g.seller
                """;
        sql += condition;
        Query q = em.createQuery(sql, Bid.class);
        return (List<Bid>) q.getResultList();
>>>>>>> 2bdb5af ([feat] 관리자 거래중 페이지 기능 구현 완료)
    }
}
