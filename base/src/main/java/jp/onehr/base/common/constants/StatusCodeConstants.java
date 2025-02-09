package jp.onehr.base.common.constants;

public interface StatusCodeConstants {

    // requestSuccess
    int DEFAULT_SUCCESS_CODE = 200;

    // serverInternalError
    int SERVER_INTERNAL_ERROR = 500;
    // objSerialize2JsonFailed
    int OBJ_SERIALIZE_2_JSON_FAILED = 5001;
    // jsonDeserialize2objFailed
    int JSON_DESERIALIZE_2_OBJ_FAILED = 5002;
    // deepCopyObjFailed
    int DEEP_COPY_OBJ_FAILED = 5003;

}
