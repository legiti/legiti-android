package com.inspetor

enum class AccountAction: EnumValue {
    ACCOUNT_CREATE_ACTION {
        override fun rawValue(): String = "account_create"
    },
    ACCOUNT_UPDATE_ACTION {
        override fun rawValue(): String = "account_update"
    },
    ACCOUNT_DELETE_ACTION {
        override fun rawValue(): String = "account_delete"
    }
}

enum class AuthAction: EnumValue {
    ACCOUNT_LOGIN_ACTION {
        override fun rawValue(): String = "account_login"
    },
    ACCOUNT_LOGOUT_ACTION {
        override fun rawValue(): String = "account_logout"
    }
}

enum class EventAction: EnumValue {
    EVENT_CREATE_ACTION {
        override fun rawValue(): String = "event_create"
    },
    EVENT_UPDATE_ACTION {
        override fun rawValue(): String = "event_update"
    },
    EVENT_DELETE_ACTION {
        override fun rawValue(): String = "event_delete"
    }
}

enum class PassRecoveryAction: EnumValue {
    PASSWORD_RESET_ACTION {
        override fun rawValue(): String = "password_reset"
    },
    PASSWORD_RECOVERY_ACTION {
        override fun rawValue(): String = "password_recovery"
    },
}

enum class SaleAction: EnumValue {
    SALE_CREATE_ACTION {
        override fun rawValue(): String = "sale_creation"
    },
    SALE_UPDATE_STATUS_ACTION {
        override fun rawValue(): String = "sale_update"
    }
}

enum class TransferAction: EnumValue {
    TRANSFER_CREATE_ACTION {
        override fun rawValue(): String = "transfer_create"
    },
    TRANSFER_UPDATE_STATUS_ACTION {
        override fun rawValue(): String = "transfer_update_status"
    }
}