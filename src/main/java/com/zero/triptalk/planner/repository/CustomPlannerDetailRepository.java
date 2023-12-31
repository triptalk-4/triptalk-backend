package com.zero.triptalk.planner.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.zero.triptalk.like.entity.QPlannerLike.plannerLike;
import static com.zero.triptalk.planner.entity.QPlanner.planner;
import static com.zero.triptalk.planner.entity.QPlannerDetail.plannerDetail;
import static com.zero.triptalk.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class CustomPlannerDetailRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tuple> getPlannerListByLikeAndViewUpdateDt(LocalDateTime from, LocalDateTime to) {

        return queryFactory.select(planner, plannerLike.likeCount)
                .from(planner)
                .leftJoin(plannerLike).on(planner.eq(plannerLike.planner))
                .leftJoin(userEntity).on(userEntity.eq(planner.user))
                .where(plannerLike.likeDt.between(from, to)
                        .or(planner.modifiedAt.between(from, to)))
                .groupBy(planner.plannerId)
                .fetch();
    }

    public List<Tuple> getPlannerDetailListByPlannerId(List<Long> ids) {

        return queryFactory.select(plannerDetail, plannerLike.likeCount)
                .from(plannerDetail)
                .join(plannerDetail.images).fetchJoin()
                .join(plannerDetail.place).fetchJoin()
                .leftJoin(plannerLike).on(plannerLike.planner.eq(plannerDetail.planner))
                .where(plannerDetail.planner.plannerId.in(ids))
                .fetch();
    }


    public void deletePlannerDetail(List<Long> updateIds, Long plannerId) {

        queryFactory.delete(plannerDetail)
                .where(plannerDetail.planner.plannerId.eq(plannerId))
                .where(plannerDetail.plannerDetailId.notIn(updateIds))
                .execute();

    }

}
