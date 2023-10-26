package com.example.comparedir;

public enum FileTypeEnum {

    TXT,XML,PROPERTIES,SH,CONF,YAML,YML;

    public static Boolean exist(String fileType) {

        for(FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if(fileTypeEnum.name().toLowerCase().equals(fileType)) {
                return true;
            }
        }
        return false;
    }
}
