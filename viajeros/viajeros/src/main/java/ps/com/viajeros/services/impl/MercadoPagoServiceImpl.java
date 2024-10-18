package ps.com.viajeros.services.impl;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;
import ps.com.viajeros.dtos.payments.PreferenceTripDto;
import ps.com.viajeros.services.MercadoPagoService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoServiceImpl implements MercadoPagoService {



    public MercadoPagoServiceImpl() {
        // Agrega tus credenciales de acceso
        MercadoPagoConfig.setAccessToken("APP_USR-8790603519044330-082821-00f71db557de45062431b4eaca8d0664-62624629");
    }

    public Preference crearPreferencia(PreferenceTripDto preferenceTrip) {
        try {
            // Crear el ítem de la preferencia usando los datos recibidos
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("123")
                    .title(preferenceTrip.getTitle()) // Usar la descripción del pedido
                    .description(preferenceTrip.getDescription())
                    .quantity(1) // Usar la cantidad del pedido
                    .currencyId("ARS")
                    .unitPrice(preferenceTrip.getMonto()) // Usar el precio del pedido
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            String successUrl = "http://localhost:4200/payment-confirmation?idViaje=" + preferenceTrip.getIdviaje();
            String pendingUrl = "http://localhost:4200/payment-confirmation?idViaje=" + preferenceTrip.getIdviaje();
            String failureUrl = "http://localhost:4200/payment-confirmation?idViaje=" + preferenceTrip.getIdviaje();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .pending(pendingUrl)
                    .failure(failureUrl)
                    .build();


            // Crear la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .build();

            PreferenceClient client = new PreferenceClient();
            return client.create(preferenceRequest);

        } catch (MPException | MPApiException e) {
            e.printStackTrace(); // Maneja la excepción según sea necesario
            return null; // O lanza una excepción personalizada o maneja el error de otra manera
        }
    }





}
