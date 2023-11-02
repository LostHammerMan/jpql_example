package org.example;

import org.example.domain.Member;
import org.example.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class Main5 {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        // 경로 표현식
        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(teamA);

            em.persist(member2);

            em.flush();
            em.clear();

            // 상태 필드
            String query = "select m.username from Member m";
            List<String> result = em.createQuery(query, String.class)
                            .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            // 단일 값 연관 경로
            String query2 = "select m.team from Member m";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            for (Team s2 : result2) {
                System.out.println("s = " + s2);
            }

            // 컬렉션 값 연관 경로
            String query3 = "select t.members from Team t";
            Collection result3 = em.createQuery(query3, Collection.class)
                    .getResultList();

            for (Object s3 : result2) {
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
