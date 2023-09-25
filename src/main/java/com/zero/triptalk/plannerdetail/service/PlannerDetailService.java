package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailListResponse;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import com.zero.triptalk.plannerdetail.repository.PlannerDetailRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.PLANNER_DETAIL_NOT_FOUNT;
import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.UNMATCHED_USER_PLANNER;
import static com.zero.triptalk.exception.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final UserRepository userRepository;
    private final PlaceService placeService;
    private final ImageService imageService;


    public List<PlannerDetailListResponse> getAllPlannerDetail() {

        List<PlannerDetail> detailList = plannerDetailRepository.findAll();

        return PlannerDetailListResponse.of(detailList);
    }

    @Transactional
    public boolean createPlannerDetail(Long planId, List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(USER_NOT_FOUND));
        //place 저장
        Place place = placeService.savePlace(request.getPlaceInfo());

        //상세 일정 저장
        PlannerDetail plannerDetail = PlannerDetail.buildPlannerDetail(
                planId, request, user, place);

        //사진 저장
        if (!files.isEmpty()) {
            imageService.uploadFiles(files, plannerDetail);
        }
        plannerDetailRepository.save(plannerDetail);
        return true;
    }

//
//    public boolean createPlannerDetailList(Long planId, List<MultipartFile> files,
//                                           List<PlannerDetailRequest> requests, String email) {
//
//        // file 검증
//
//        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
//                new UserException(USER_NOT_FOUND));
//
//        List<PlannerDetail> detailList = new ArrayList<>();
//        for (PlannerDetailRequest x : requests) {
//
//            PlannerDetail plannerDetail = PlannerDetail.builder()
//                    .plannerId(planId)
//                    .userId(user.getUserId())
//                    .date(x.getDate())
//                    .time(x.getTime())
//                    .description(x.getDescription())
//                    .build();
//            detailList.add(plannerDetail);
//        }
//        plannerDetailRepository.saveAll(detailList);
//
//        return true;
//    }

    public boolean updatePlannerDetail(List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        // file 검증

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(request.getId()).orElseThrow(() ->
                new PlannerDetailException(PLANNER_DETAIL_NOT_FOUNT));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        //장소 업데이트

        plannerDetail.updatePlannerDetail(request);

        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public boolean deletePlannerDetail(Long detailId, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(detailId)
                .orElseThrow(() -> new PlannerDetailException(PLANNER_DETAIL_NOT_FOUNT));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        plannerDetailRepository.deleteById(plannerDetail.getId());

        return true;
    }
}