package com.legiti.helpers

enum class UserAction: EnumValue {
    USER_CREATE_ACTION {
        override fun rawValue(): String = "user_create"
    },
    USER_UPDATE_ACTION {
        override fun rawValue(): String = "user_update"
    }
}

enum class AuthAction: EnumValue {
    USER_LOGIN_ACTION {
        override fun rawValue(): String = "user_login"
    },
    USER_LOGOUT_ACTION {
        override fun rawValue(): String = "user_logout"
    }
}

enum class PasswordAction: EnumValue {
    PASSWORD_RESET_ACTION {
        override fun rawValue(): String = "password_reset"
    },
    PASSWORD_RECOVERY_ACTION {
        override fun rawValue(): String = "password_recovery"
    },
}

enum class OrderAction: EnumValue {
    ORDER_CREATE_ACTION {
        override fun rawValue(): String = "order_create"
    }
}
