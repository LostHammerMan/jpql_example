package org.example;

import org.example.domain.Member;
import org.example.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main6 {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em  = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            // 페치 조인 사용 X
//            String query = "select m From Member m";
//            List<Member> result = em.createQuery(query, Member.class).getResultList();
//
//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
//                // 회원1, 팀A(SQL)
//                // 회원2, 팀A(1차 캐시)
//                // 회원3, 팀B(SQL)
//
//                // 회원 100명, 팀이 다른 경우 -> 쿼리 100번 나가는 문제 발생( N+1 ) -> 페치 조인 필요
//            }
//
//            // 페치 조인 사용 O
//            String query2 = "select m From Member m join fetch m.team";
//            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();
//
//
//            for (Member member : result2) {
//                // 페치 조인으로 회원과 팀을 함꼐 조회해서 지연로딩 X
//                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
//            }
//
//            // 컬렉션 페치 조인
//            String query3 = "select t From Team t join fetch t.members";
//            List<Team> result3 = em.createQuery(query3, Team.class).getResultList();
//
//
//            for (Team team : result3) {
//                System.out.println("team= " + team.getName() + ", members = " + team.getMembers().size());
//                for (Member member : team.getMembers()){
//                    System.out.println("member = " + member);
//                }
//            }

            // 페치 조인과 DISTINCT
            String query4 = "select distinct t From Team t join fetch t.members";
            List<Team> result4 = em.createQuery(query4, Team.class).getResultList();


            for (Team team : result4) {
                System.out.println("team= " + team.getName() + ", members = " + team.getMembers().size());
                for (Member member : team.getMembers()){
                    System.out.println("\t member = " + member);
                }
            }

            // 페치 조인과 일반 조인의 차이
            String query5 = "select  t From Team t join t.members m";
            List<Team> result5 = em.createQuery(query5, Team.class).getResultList();

            // 페치 조인 대상에는 별칭을 주면 안됨
            String query6 = "select  t From Team t join fetch t.members m";
            List<Team> result6 = em.createQuery(query6, Team.class).getResultList();

            // 컬렉션 페치 조인과 페이징
            // 페치 조인 대상에는 별칭을 주면 안됨
            String query7 = "select  t From Team t join fetch t.members m";
            List<Team> result7 = em.createQuery(query7, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();


            for (Team team : result5) {
                System.out.println("team= " + team.getName() + ", members = " + team.getMembers().size());
                for (Member member : team.getMembers()){
                    System.out.println("\t member = " + member);
                }
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
