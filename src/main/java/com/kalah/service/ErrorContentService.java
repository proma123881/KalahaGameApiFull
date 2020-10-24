package com.kalah.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Class for fetching error description from errors.yaml
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorContentService {

    public static final String PROPERTY_DESCRIPTION = ".description";
    private final PropertySource errorContent;


    /**
     * Method to fetch error description for a given error code
     *
     * @param errorCode Unique identifier for a particular error
     * @return error description for the given error code
     */
    public String getErrorDescription(String errorCode) {
        return getErrorDescription(errorCode, null);

    }

    /**
     * Method to fetch error description along with parameters
     * for a given error code
     *
     * @param errorCode Unique identifier for a particular error
     * @param parameters Parameters passed along with error description
     * @return error description for the given error code
     */
    public String getErrorDescription(String errorCode, List<String> parameters) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorContentService::getErrorDescription - errorCode must be specified");
        }
        String errorDescription = (String) errorContent.getProperty(errorCode + PROPERTY_DESCRIPTION);
        if (StringUtils.isBlank(errorDescription)) {
            log.error("Unable to retrieve error description for error code {}", errorCode);
            errorDescription = "";
        }
        return parameters == null ? errorDescription : String.format(errorDescription, parameters.toArray());
    }


}
