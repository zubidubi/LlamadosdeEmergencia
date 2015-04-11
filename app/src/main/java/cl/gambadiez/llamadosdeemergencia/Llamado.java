package cl.gambadiez.llamadosdeemergencia;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Llamado implements Parcelable {
    private String clave;
    private String sector;
    private String direccion;
    private String unidades;
    private Date date;
    private int iconResourceID;

    public Llamado (String clave, String sector, String direccion, String unidades, Date date)
    {
        super();
        this.clave = clave;
        this.sector = sector;
        this.direccion = direccion;
        this.unidades = unidades;
        this.date = date;
        setIconResouceIDFromClave();
    }

    public String getClave()
    {
        return clave;
    }
    public String getSector()
    {
        return sector;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public String getUnidades()
    {
        return  unidades;
    }

    public Date getDate()
    {
        return date;
    }

    public int getIconResourceID()
    {
        return iconResourceID;
    }

    public void setIconResouceIDFromClave()
    {
        //TODO:: obtener el numero de la clave
        int numeroClave = 0;
        switch (numeroClave)
        {
            case 0:
                this.iconResourceID = R.drawable.firewall;
                break;
            //TODO:: hacer mas casos
        }
    }

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(clave);
        out.writeString(sector);
        out.writeString(direccion);
        out.writeString(unidades);
        out.writeLong(date.getTime());
        out.writeInt(iconResourceID);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Llamado> CREATOR = new Parcelable.Creator<Llamado>() {
        public Llamado createFromParcel(Parcel in) {
            return new Llamado(in);
        }

        public Llamado[] newArray(int size) {
            return new Llamado[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Llamado(Parcel in) {
        clave = in.readString();
        sector = in.readString();
        direccion = in.readString();
        unidades = in.readString();
        date = new Date(in.readLong());
        iconResourceID = in.readInt();
    }
}
