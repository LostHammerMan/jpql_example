package org.example;

import org.example.domain.Member;
import org.example.domain.MemberType;
import org.example.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main3 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team teamB = new Team();
            teamB.setName("teamB");

            em.persist(teamB);

            Member member2 = new Member();
            member2.setUsername("member3");
            member2.setType(MemberType.USER);
            member2.setAge(20);
            member2.setTeam(teamB);

            Member member3 = new Member();
            member3.setUsername("관리자");
            member3.setType(MemberType.ADMIN);
            member3.setAge(30);
            member3.setTeam(teamB);

            em.persist(member2);
            em.persist(member3);

            // 조건식 case 식
            String query = "select " +
                    "case when m.age <= 10 then '학생요금' " +
                        "   when m.age >= 60 then '경로요금' " +
                        "   else '일반요금' END " +
                    "from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s1 = " + s);
            }
            System.out.println("=======================");
            // COALEASCE
            String query2 = "select coalesce(m.username, '이름 없는 회원') as username from Member m ";
            List<String> result2 = em.createQuery(query2, String.class).getResultList();

            for (String s2 : result2) {
                System.out.println("s2 = " + s2);
            }
            System.out.println("==========================");

            // NULLIF
            String query3 = "select nullif(m.username, '관리자') as username from Member m ";
            List<String> result3 = em.createQuery(query3, String.class).getResultList();

            for (String s3 : result3) {
                System.out.println("s3 = " + s3);
            }
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
