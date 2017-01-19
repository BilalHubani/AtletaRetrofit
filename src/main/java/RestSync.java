import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by dam on 19/1/17.
 */
public class RestSync {
    private static Retrofit retrofit;
    public static void main(String[] args) throws IOException {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AtletaService atletaService = retrofit.create(AtletaService.class);
        Response<List<Atleta>> responseAllAtletas = atletaService.getAllAtleta().execute();
        if (responseAllAtletas.isSuccessful()){
            List<Atleta>listaAtleta = responseAllAtletas.body();
            System.out.println("Status code: "+responseAllAtletas.code()+System.lineSeparator()+"GET all atletas: "+listaAtleta);
        }else {
            System.out.println("Status code: "+responseAllAtletas.code()+"Mensage de error: "+responseAllAtletas.errorBody());
        }
        Response<List<Atleta>> responseUrlError = atletaService.getError().execute();
        if (!responseUrlError.isSuccessful()) {
            System.out.println("Status code: " + responseUrlError.code() + " Mensage de error: " + responseUrlError.raw());
        }
        Atleta atleta = new Atleta();
        atleta.setNombre("Pikachu");
        atleta.setApellido("Raichu");
        atleta.setNacionalidad("Kanto");
        atleta.setNacimiento(LocalDate.of(1994,10,25));
        Response<Atleta> postAtletas = atletaService.createAtleta(atleta).execute();
        if (postAtletas.isSuccessful()){
            Atleta atletaRespuesta = postAtletas.body();
            System.out.println("Status code: "+ postAtletas.code()+System.lineSeparator()+"POST Atleta: "+atletaRespuesta);
            Response<Atleta>respuestaUnAtleta = atletaService.getAtleta(atletaRespuesta.getId()).execute();
            if (respuestaUnAtleta.isSuccessful()){
                System.out.println("GET ONE->Status code: "+respuestaUnAtleta+" Atleta: "+respuestaUnAtleta);
            }else {
                System.out.println("Status code: "+respuestaUnAtleta.code()+"Mensage de error: "+respuestaUnAtleta.errorBody());
            }
            atletaRespuesta.setNacionalidad("Jhoto");
            Response<Atleta> ponerAtleta = atletaService.updateAtleta(atletaRespuesta).execute();
            if (respuestaUnAtleta.isSuccessful()){
                System.out.println("Status code: "+ponerAtleta.code()+System.lineSeparator()+"PUT Atleta: "+ponerAtleta.body());
            }else {
                System.out.println("Status code: "+ponerAtleta.code()+System.lineSeparator()+"Mensage de error: "+ponerAtleta.errorBody());
            }
            Response<Void>borrarAtleta=atletaService.deleteAtleta(atletaRespuesta.getId()).execute();
            System.out.println("DELETE status code: "+borrarAtleta.code());
            responseAllAtletas=atletaService.getAllAtleta().execute();
            System.out.println("Comprobar delete, status code: "+responseAllAtletas.code()+System.lineSeparator()+"GET Atletas: "+responseAllAtletas.body());
        }
    }
}
