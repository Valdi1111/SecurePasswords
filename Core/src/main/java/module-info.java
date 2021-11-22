module org.valdi.securepasswords.core {
    requires com.google.gson;

    opens org.valdi.securepasswords.core.auth to com.google.gson;
    opens org.valdi.securepasswords.core.objects to com.google.gson;

    exports org.valdi.securepasswords.core;
    exports org.valdi.securepasswords.core.auth;
    exports org.valdi.securepasswords.core.objects;
}