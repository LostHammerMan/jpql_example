package org.example;

import com.mysql.cj.xdevapi.AddResult;
import org.example.domain.Address;
import org.example.domain.Member;
import org.example.domain.Order;
import org.example.domain.Team;
import org.example.domain.dto.MemberDto;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {

            for (int i = 0; i<100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }
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
            // 팀 조회시 조인 이용 --> 조인은 명시적으로 하는 것이 좋다
//            List<Team> result2 = em.createQuery("select m.team from Member as m join m.team t", Team.class)
            List<Team> result2 = em.createQuery("select t from Member as m join m.team t", Team.class)
                    .getResultList();

            // 임베디드 타입 프로젝션
            List<Address> result3 = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            // 스칼라 타입 프로젝션
            List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

            Object o  = resultList.get(0);
            Object[] result4 = (Object[]) o;
            System.out.println("username =  " + result4[0]);
            System.out.println("age =  " + result4[1]);

            // 제네릭 사용시 위 과정 생략 가능
            List<Object[]> resultList2 = em.createQuery("select m.username, m.age from Member m").getResultList();
            Object[] result5 = resultList2.get(0);
            System.out.println("username =  " + result5[0]);
            System.out.println("age =  " + result5[1]);

            // new 명령어로 조회
            List<MemberDto> resultList3 = em.createQuery("select new org.example.domain.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class).getResultList();
            MemberDto memberDto = resultList3.get(0);
            System.out.println("memberDto = " + memberDto.getUsername());
            System.out.println("memberDto = " + memberDto.getAge());

            // 페이징 예시
            List<Member> result6 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                            .setFirstResult(0)
                            .setMaxResults(10)
                            .getResultList();

            System.out.println("result6.size = " + result6.size());

            for (Member memberList : result6) {
                System.out.println("memberList = " + memberList);
            }

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