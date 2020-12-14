package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;

import java.util.HashMap;
import java.util.List;

public class UtilService {

    public static HashMap<String, Object> mapListToDto(List<FormSubmissionFieldDTO> list)
    {
        HashMap<String, Object> map = new HashMap<>();
        for(FormSubmissionFieldDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }
}
