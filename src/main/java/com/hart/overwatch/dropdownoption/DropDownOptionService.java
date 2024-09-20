package com.hart.overwatch.dropdownoption;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.customfield.CustomField;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionPayloadDto;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class DropDownOptionService {


    private final DropDownOptionRepository dropDownOptionRepository;


    @Autowired
    public DropDownOptionService(DropDownOptionRepository dropDownOptionRepository) {
        this.dropDownOptionRepository = dropDownOptionRepository;
    }

    private DropDownOption getDropDownOptionById(Long dropDownOptionId) {
        return dropDownOptionRepository.findById(dropDownOptionId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find dropdown option with id %d", dropDownOptionId)));
    }

    public void createDropDownOptions(List<DropDownOptionPayloadDto> dropDownOptions,
            CustomField customField) {
        for (DropDownOptionPayloadDto dropDownOptionDto : dropDownOptions) {
            String optionValue = Jsoup.clean(dropDownOptionDto.getValue(), Safelist.none());
            DropDownOption dropDownOption = new DropDownOption(optionValue, customField);

            dropDownOptionRepository.save(dropDownOption);
        }
    }

    public void deleteDropDownOption(Long dropDownOptionId) {
        DropDownOption dropDownOption = getDropDownOptionById(dropDownOptionId);

        dropDownOptionRepository.delete(dropDownOption);
    }
}
