package com.metacoding.web_project.bid;

import com.metacoding.web_project._core.CommonResp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class BidController {
    private final BidService bidService;

    // 로그인 구현 시 경로를 /admin/auction-progress 로 변경 예정
    @GetMapping("/auction-progress")
    public String auctionProgress() {
        return "admin/auction-progress-admin";
    }

    @PostMapping("/catchDetailPageData")
    public ResponseEntity<?> uploadBidData(@RequestBody BidRequest.TryBidDTO tryBidDTO) {
        bidService.saveTryPrice(tryBidDTO);
        //Resp resp = new Resp(true, "성공", null);
        CommonResp resp = new CommonResp(true, "성공", null);
        return ResponseEntity.ok(resp);
    }
}
