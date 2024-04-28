package com.hart.overwatch.setting;

import org.springframework.stereotype.Service;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public Setting createSetting() {
        Setting setting = new Setting();

        this.settingRepository.save(setting);

        return setting;
    }
}

