package org.example;

import org.example.domain.Member;
import org.example.domain.MemberType;
import org.example.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

// JPQL 함수
public class Main4 {
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

            em.flush();
            em.clear();

            // JPQL 기본 함수
            // concat
            String query = "select concat('a','b') from Member m";
//            List<String> result = em.createQuery(query, String.class)
//                            .getResultList();

            /*for (String s : result) {
                System.out.println("s = " + s);
            }*/

            // subString
            String query2 = "select substring(m.username,1,2) from Member m";
            List<String> result2 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s2 : result2) {
                System.out.println("s2 = " + s2);
            }
            System.out.println("=========================");

            // TRIM
//            String query3 = "select trim() from Member m";
//            List<String> result3 = em.createQuery(query3, String.class)
//                    .getResultList();

            // locate
//            String query4 = "select locate('ab', 'abcdefg') from Member m";
//            List<Integer> result4 = em.createQuery(query4, Integer.class)
//                    .getResultList();
//
//            for (Integer s3 : result4) {
//                System.out.println("integer = " + s3);
//            }

            // SIZE - 컬렉션의 크기 반환
            String query5 = "select size(t.members) from Team t ";
            List<Integer> result5 = em.createQuery(query5, Integer.class)
                            .getResultList();

            for (Integer s4 : result5) {
                System.out.println("s4 = " + s4);
            }

            // 사용자 정의 함수 호출
            String query6 = "select function('group_concat', m.username) from Member m ";
            List<String> result6 = em.createQuery(query6, String.class)
                    .getResultList();

            for (String s5 : result6) {
                System.out.println("s5 = " + s5);
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
