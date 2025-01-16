package com.example.reto_8.ui.screens.enterpriseList.events

import com.example.reto_8.model.Enterprise

sealed interface EnterpriseListUiEvent {
    data object GetEnterprises: EnterpriseListUiEvent
    data class SearchEnterprises(val searchQuery: String): EnterpriseListUiEvent
    data class InsertEnterprise(val enterprise: Enterprise): EnterpriseListUiEvent
    data class UpdateEnterprise(val newEnterprise: Enterprise): EnterpriseListUiEvent
    data class DeleteEnterprise(val enterprise: Enterprise): EnterpriseListUiEvent
}
