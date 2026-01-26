package network
import com.example.portable.model.FichaResponse
import com.example.portable.model.LoginRequest
import com.example.portable.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("fichas/usuario/{usuario_id}")
    fun getSheets(@Path("usuario_id") usuarioId: Int): Call<List<FichaResponse>>
}
