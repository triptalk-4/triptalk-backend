package com.zero.triptalk.search.controller;

import com.zero.triptalk.planner.dto.PlannerSearchResponse;
import com.zero.triptalk.planner.dto.PlannerDetailSearchResponse;
import com.zero.triptalk.search.service.SearchService;
import com.zero.triptalk.user.dto.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/main")
    public ResponseEntity<List<PlannerSearchResponse>> getTop6Planners() {

        return ResponseEntity.ok(searchService.getTop6PlannersWithLikes());
    }

    @GetMapping("/search/{region}/{searchType}")
    public ResponseEntity<List<PlannerDetailSearchResponse>> searchByRegionAndSearchType(
            @PathVariable String region, @PathVariable String searchType, Pageable pageable) {

     return ResponseEntity.ok(searchService.searchByRegionAnySort(region, searchType, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> getUserNicknameList(@RequestParam String keyword, Pageable pageable) {

        return ResponseEntity.ok(searchService.getUserNicknameList(keyword, pageable));
    }

}
