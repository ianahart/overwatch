package com.hart.overwatch.setting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.setting.request.UpdateSettingMfaEnabledRequest;
import com.hart.overwatch.setting.request.UpdateSettingRequest;
import com.hart.overwatch.setting.response.GetSettingResponse;
import com.hart.overwatch.setting.response.UpdateSettingMfaEnabledResponse;
import com.hart.overwatch.setting.response.UpdateSettingResponse;

@RestController
@RequestMapping(path = "/api/v1/settings")
public class SettingController {

    private final SettingService settingService;


    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }


    @GetMapping(path = "/{settingId}")
    ResponseEntity<GetSettingResponse> getSetting(@PathVariable("settingId") Long settingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetSettingResponse("success", this.settingService.getSetting(settingId)));
    }


    @PatchMapping(path = "/{settingId}/mfa-enabled")
    ResponseEntity<UpdateSettingMfaEnabledResponse> updateSettingMfaEnabled(
            @PathVariable("settingId") Long settingId,
            @RequestBody UpdateSettingMfaEnabledRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(new UpdateSettingMfaEnabledResponse(
                "success",
                this.settingService.updateSettingMfaEnabled(settingId, request.getMfaEnabled())));
    }

    @PutMapping(path = "/{settingId}")
    ResponseEntity<UpdateSettingResponse> updateSetting(@PathVariable("settingId") Long settingId,
            @RequestBody UpdateSettingRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateSettingResponse("success",
                settingService.updateSetting(request, settingId)));
    }

}
