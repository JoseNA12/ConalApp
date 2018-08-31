package cr.ac.tec.conalapp.conalapp.Utilitarios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cr.ac.tec.conalapp.conalapp.Modelo.ContactosModelo;

public class LeerArchivoContactos
{
    public ArrayList<ContactosModelo> array_contactos;

    public LeerArchivoContactos(InputStream pInputStream)
    {
        BufferedReader reader = null;
        array_contactos = new ArrayList<>();

        try
        {
            reader = new BufferedReader(
                    new InputStreamReader(pInputStream));

            // do reading, usually loop until end of file reading
            String mLinea;
            while ((mLinea = reader.readLine()) != null)
            {
                llenarListaObjetosContacto(mLinea);
            }
        }
        catch (IOException e) { }
        finally
        {
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) { }
            }
        }
    }

    private void llenarListaObjetosContacto(String mLinea)
    {
        String[] parts = mLinea.split(",");
        array_contactos.add(new ContactosModelo(parts[0], parts[1], parts[2], parts[3]));
    }
}
