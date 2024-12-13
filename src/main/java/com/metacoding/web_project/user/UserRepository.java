package com.metacoding.web_project.user;

import com.metacoding.web_project._core.error.ex.Exception401;
import com.metacoding.web_project._core.error.ex.Exception404;
import com.metacoding.web_project.useraccount.UserAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public User findById(int id) {
        Query q = em.createQuery("select u from User u where u.id = :id", User.class);
        q.setParameter("id", id);

        try {
            return (User) q.getSingleResult();
        } catch (RuntimeException e) {
            throw new RuntimeException("유저가 없어요");
        }
    }

    public User findByUsername(String username) {
        Query q = em.createQuery("select u from User u where u.username = :username", User.class);
        q.setParameter("username", username);

        try {
            return (User) q.getSingleResult();
        } catch (RuntimeException e) {
            throw new Exception404("유저를 찾을 수 없습니다.");
        }
    }

    public void join(User user) {
        em.persist(user);
    }

    public void join2(UserAccount userAccount) {
        em.persist(userAccount);
    }

    public int checkId(String username) {
        Query q = em.createQuery("select count(u) from User u where u.username = :username");
        q.setParameter("username", username);
        Long count = (Long) q.getSingleResult();
        System.out.println(count);
        return count.intValue();
    }


    public UserAccount findInfo(int id) {
        Query q = em.createQuery("select u from UserAccount u right join fetch u.user where u.user.id = :id ", UserAccount.class);
        q.setParameter("id", id);
        return (UserAccount) q.getSingleResult();
    }

    public void updateUser(int id, String tel, String postNum, String addr, String addrDetail, String account) {
        String sql = """
                update User u
                set u.tel = :tel,
                    u.postNum = :postNum,
                    u.addr = :addr,
                    u.addrDetail = :addrDetail
                where u.id = :id
                """;

        Query q = em.createQuery(sql);
        q.setParameter("id", id);
        q.setParameter("tel", tel);
        q.setParameter("postNum", postNum);
        q.setParameter("addr", addr);
        q.setParameter("addrDetail", addrDetail);
        q.executeUpdate();
    }

    public void updateUserAccount(int id, String tel, String postNum, String addr, String addrDetail, String account) {
        String Sql2 = """
            update UserAccount u
            set u.account = :account
            where u.user.id = :id
            """;
        System.out.println(account);
        Query qu = em.createQuery(Sql2);
        qu.setParameter("id", id);
        qu.setParameter("account", account);
        qu.executeUpdate();
    }

    @Transactional
    public Optional<User> changePw(int id) {

        Query q = em.createQuery("select u from User u where u.id = :id");
        q.setParameter("id", id);
        try{
            User user = (User) q.getSingleResult();
            return Optional.ofNullable(user);
        }catch (RuntimeException e){
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<UserAccount> findByIdUserInfo(int id) {
        String sql = """
                    select u from UserAccount u join fetch u.user where u.user.id = :id 
                    """;
        Query q = em.createQuery(sql);
        q.setParameter("id", id);
            try{
                UserAccount userAccount = (UserAccount) q.getSingleResult();
                return Optional.ofNullable(userAccount);
            }catch (RuntimeException e){
                return Optional.empty();
            }
    }

    public String  findUserId(String tel, String name){
        try {
        String sql = """
            select u.username from User u where u.name = :name and u.tel = :tel
            """;
        Query q = em.createQuery(sql, String.class);
        q.setParameter("name", name);
        q.setParameter("tel", tel);
            return (String) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Integer findPassword(String tel, String name, String username){
        try{
            String sql = """
                select u.id from User u where tel = :tel and name =:name and username =:username
                """;
            Query q = em.createQuery(sql);
            q.setParameter("tel", tel);
            q.setParameter("name", name);
            q.setParameter("username", username);

            return (Integer) q.getSingleResult();
        }catch (NoResultException e){
            return 0;
        }
    }

    //비밀번호 변경하기
    public void changePassword(int id, String Password) {
        Query q = em.createQuery("update User u set u.password = :newPassword where u.id = :id");
        q.setParameter("id",id);
        q.setParameter("newPassword",Password);
        q.executeUpdate();
    }


    public User findById(Integer id){
        Query q = em.createQuery("select u from User u where u.id = :id", User.class);
        q.setParameter("id", id);
        return (User) q.getSingleResult();
    }
}