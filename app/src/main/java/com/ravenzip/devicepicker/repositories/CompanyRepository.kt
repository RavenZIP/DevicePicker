package com.ravenzip.devicepicker.repositories

import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.model.company.Employee
import com.ravenzip.devicepicker.sources.CompanySources
import com.ravenzip.kotlinflowextended.functions.materialize
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

@Singleton
class CompanyRepository
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val companySources: CompanySources,
) {
    fun getCompanies() =
        flow {
                val response = companySources.companyBaseSource().get().await().children
                val companies = response.convertToClass<Company>()

                emit(companies)
            }
            .materialize()

    fun getCompanyByUid(uid: String) =
        flow {
                val response = companySources.companyByUid(uid).get().await()
                val convertedResponse = response.getValue<Company>()

                if (convertedResponse == null) {
                    throw Throwable("При загрузке данных о компании произошла ошибка")
                }

                emit(convertedResponse)
            }
            .materialize()

    fun addCompany(
        name: String,
        description: String,
        address: String,
        code: String,
        fullName: String,
    ) =
        flow {
                val uid = companySources.companyBaseSource().push().key.toString()
                val leader = Employee.createLeader(authRepository.firebaseUser!!.uid, fullName)
                val company = Company.create(uid, name, description, address, leader, code)

                companySources.companyByUid(uid).setValue(company).await()
                emit(uid)
            }
            .materialize()

    fun joinToCompany(companyUid: String, fullName: String) =
        flow {
                val employee = Employee.createEmployee(authRepository.firebaseUser!!.uid, fullName)

                companySources.requestToJoin(companyUid).push().setValue(employee).await()
                emit(companyUid)
            }
            .materialize()

    fun addRequestToJoinInCompany(companyUid: String) =
        flow {
                companySources
                    .requestToJoin(companyUid)
                    .push()
                    .setValue(authRepository.firebaseUser!!.uid)
                    .await()

                emit(companyUid)
            }
            .materialize()

    fun leaveCompany(companyUid: String, employees: List<Employee>) =
        flow { emit(companySources.employees(companyUid).setValue(employees).await()) }
            .materialize()

    fun deleteCompany(companyUid: String) =
        flow { emit(companySources.companyByUid(companyUid).removeValue().await()) }.materialize()
}
