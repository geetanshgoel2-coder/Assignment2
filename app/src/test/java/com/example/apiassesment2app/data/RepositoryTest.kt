package com.example.apiassesment2app.data

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var api: ApiService
    private lateinit var repo: Repository

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val client = OkHttpClient()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(ApiService::class.java)
        repo = Repository(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun login_success_returns_keypass() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(200).setBody("""{"keypass":"forex"}"""))

        val res = repo.login("footscray", "Geetansh", "8103430")
        assertThat(res.keypass).isEqualTo("forex")

        val req = server.takeRequest()
        assertThat(req.method).isEqualTo("POST")
        assertThat(req.path).isEqualTo("/footscray/auth")
        assertThat(req.body.readUtf8()).contains("\"username\":\"Geetansh\"")
    }

    @Test
    fun dashboard_success_parses_entities() = runBlocking {
        val body = """
            {
              "entities":[{"assetType":"Stock","ticker":"AAPL","description":"Apple"}],
              "entityTotal":1
            }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val res = repo.dashboard("forex")
        assertThat(res.entityTotal).isEqualTo(1)
        assertThat(res.entities.first()["assetType"]).isEqualTo("Stock")

        val req = server.takeRequest()
        assertThat(req.method).isEqualTo("GET")
        assertThat(req.path).isEqualTo("/dashboard/forex")
    }
}
