package org.example;

import org.example.domain.Member;
import org.example.domain.Team;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            em.persist(member1);

            em.flush();
            em.clear();

            // 타입 정보가 명확한 경우
//            TypedQuery<Member> query = em.createQuery("select m from Member as m", Member.class);
            TypedQuery<Member> query = em.createQuery("select m from Member as m where m.username = :username", Member.class);

            // 파라미터 바인딩 - 이름 기준
            /*Member result = em.createQuery("select m from Member as m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();*/

            // 파라미터 바인딩 - 위치 기준


            // 타입 정보가 명확하지 않은 경우
//            Query query1 = em.createQuery("select m.username, m.age from Member as m");

            // 리스트 반환
//            List<Member> resultList = query.getResultList();
//
//            for (Member result : resultList){
//                System.out.println("result = " + result);
//            }

            // 파라미터 바인딩
             //  이름 기준
           /* Member singleResult2 = query.getSingleResult();

            System.out.println("singleResult2 = " + singleResult2.getUsername());*/

          /*  // 단일 객체 반환
            Member singleResult = query.getSingleResult();
            System.out.println("singleResult = " + singleResult);*/

            // 프로젝션
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20);

            System.out.println("================================");
            // 팀 조회
            List<Team> result2 = em.createQuery("select m.team from Member as m", Team.class)
                    .getResultList();


            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
       emf.close();
    }


}