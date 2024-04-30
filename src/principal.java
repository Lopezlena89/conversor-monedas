import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
public class principal {
    private static double obtenerTipoCambio() throws IOException {
        URL url = new URL("https://api.exchangerate-api.com/v4/latest/USD");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonObject ratesObject = jsonObject.getAsJsonObject("rates");
        JsonElement mxnRateElement = ratesObject.get("MXN");
        return mxnRateElement.getAsDouble();
    }

    public static void main(String[] args) throws IOException {
        double tipoCambio;
        try {
            tipoCambio = obtenerTipoCambio();
        } catch (IOException e) {
            tipoCambio = 20.0;
            System.err.println("Error al obtener el tipo de cambio: " + e.getMessage());
            return;
        }

        Scanner teclado = new Scanner(System.in);
        int eleccionUsuario;

        do {
            String bienvenida = """
             ***********************************************************************************************************
                                                   Convertidor de Monedas                               
                                                                                                                                                                                                                            *
                1.- Convertir Peso Méxicano a Dolar Americano.                                                        
                2.- Convertir Dolar Americano a Peso Mexicano.                                                        
                3.- Salir del programa.                                                                               
                                                                                                                      
             ***********************************************************************************************************
             """;

            System.out.println(bienvenida);
            System.out.println("Escriba el número acorde a tu elección:");
            eleccionUsuario = teclado.nextInt();

            switch (eleccionUsuario) {
                case 1:
                    System.out.print("Cantidad en pesos mexicanos: ");
                    double cantidadPesos = teclado.nextDouble();
                    double dolares = cantidadPesos / tipoCambio;
                    System.out.println("Cantidad en dólares americanos es: " + dolares);
                    break;
                case 2:
                    System.out.print("Cantidad en dólares americanos: ");
                    double dolaresAmericanos = teclado.nextDouble();
                    double conversionPeso = dolaresAmericanos * tipoCambio;
                    System.out.println("Cantidad en pesos mexicanos es: " + conversionPeso);
                    break;
                case 3:
                    System.out.println("Gracias por usar el convertidor de divisas. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (eleccionUsuario != 3);

        teclado.close();
    }
}
