package network
import com.example.portable.model.FichaResponse
import com.example.portable.model.LoginRequest
import com.example.portable.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("fichas/{usuario_id}")
    fun getSheets(@Path("usuario_id") usuarioId: Int): Call<List<FichaResponse>>

    @POST("fichas")
    fun createSheet(@Body request: FichaResponse): Call<FichaResponse>

    @DELETE("fichas/{id}")
    fun deleteSheet(@Path("id") id: Int): Call<Void> // Call<Void> por não esperar retorno.

    @GET("fichas/id/{id}")
    fun getSheet(@Path("id") id: Int): Call<FichaResponse>
}
