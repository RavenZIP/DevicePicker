package com.ravenzip.devicepicker.repositories

import android.util.Log
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.model.company.Employee
import com.ravenzip.devicepicker.sources.CompanySources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
            .catch {
                withContext(Dispatchers.Main) { Log.e("getCompanies", "${it.message}") }
                emit(listOf())
            }

    fun getCompanyByUid(uid: String) =
        flow {
                val response = companySources.companyByUid(uid).get().await()
                val convertedResponse = response.getValue<Company>()

                emit(convertedResponse ?: Company())
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getCompanyByUid", "${it.message}") }
                emit(Company())
            }

    fun addCompany(name: String, description: String, address: String) =
        flow {
                val uid = companySources.companyBaseSource().push().key.toString()
                val administrator = Employee.createAdministrator(authRepository.firebaseUser!!.uid)
                val company = Company(uid, name, description, address, listOf(administrator))
                companySources.companyByUid(uid).setValue(company).await()

                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("addCompany", "${it.message}") }
                emit(false)
            }

    // TODO реализовать добавления сотрудника в компанию
    fun addEmployeeToCompany(companyUid: String, employee: Employee) =
        flow {
                companySources.companyMembers(companyUid).push().setValue(employee).await()
                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("addMemberToCompany", "${it.message}") }
                emit(false)
            }
}
