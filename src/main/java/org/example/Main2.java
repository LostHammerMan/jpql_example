package org.example;

import org.example.domain.Member;
import org.example.domain.MemberType;
import org.example.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction  tx = em.getTransaction();

        try {

            Team team  = new Team();
            team.setName("teamA");
            em.persist(team);
//            for (int i=0; i<100; i++){
            Member member = new Member();
            member.setUsername("member2");
            member.setAge(20);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);


//            }

            // 내부 조인(inner 생략 가능)
            String query = "select m from Member m inner join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                            .getResultList();
            System.out.println("======================");

            // left outer join(outer 생략 가능)
            String query2 = "select m from Member m left outer join m.team t";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();
            System.out.println("=========================");

            // 세타 조인(theta join)(막조인)
            String query3 = "select m from Member m, Team t where m.username = t.name";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .getResultList();

            System.out.println("result3 = " + result3.size());
            System.out.println("==========================");
            // ON 절 - 조인대상 필터링
            String query4 = "select m from Member m LEFT JOIN m.team t ON t.name = 'teamA'";
            List<Member> result4 = em.createQuery(query4, Member.class).getResultList();
            System.out.println("======================");

            // ON 절 - 연관관계 없는 외부 조인
            String query5 = "select m from Member m LEFT JOIN Team t ON m.username = t.name";
            List<Member> result5 = em.createQuery(query5, Member.class).getResultList();

            // select 절
            /*String query6 = "select (select avg(m1.age) from Member m1) as avgAge from Member m LEFT JOIN Team t ON m.username = t.name";
            List<Member> result6 = em.createQuery(query6, Member.class).getResultList();*/
            System.out.println("===================");
            // JPQL - 타입표현
            System.out.println("----------- jpql 타입표현 ---------------");
            String query7 = "select m.username from Member m where m.type = org.example.domain.MemberType.ADMIN";
            List<Object[]> result7 = em.createQuery(query7).getResultList();

            for (Object[] objects : result7) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }

            // 파라미터 바인딩 한 경우
            System.out.println("----------- jpql 타입표현2 ---------------");
            String query8 = "select m.username from Member m where m.type = :userType";
            List<Object[]> result8 = em.createQuery(query8)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : result8) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
