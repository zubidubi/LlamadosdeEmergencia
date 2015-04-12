package cl.gambadiez.llamadosdeemergencia;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Llamado implements Parcelable {
    private int ID;
    private String clave;
    private String sector;
    private String direccion;
    private String unidades;
    private Date date;
    private int iconResourceID;

    public Llamado()
    {}

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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getClave()
    {
        return clave;
    }

    public void  setClave(String clave) {
        this.clave = clave;
    }

    public String getSector()
    {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUnidades()
    {
        return  unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIconResourceID()
    {
        return iconResourceID;
    }

    public void setIconResourceID(int iconResourceID) {
        this.iconResourceID = iconResourceID;
    }

    public void setIconResouceIDFromClave()
    {
        //TODO:: obtener el numero de la clave
        int numeroClave = 0;
        switch (numeroClave)
        {
            case 0:
         //       this.iconResourceID = R.drawable.firewall;
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
        out.writeInt(ID);
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
        ID = in.readInt();
        clave = in.readString();
        sector = in.readString();
        direccion = in.readString();
        unidades = in.readString();
        date = new Date(in.readLong());
        iconResourceID = in.readInt();
    }

    @Override
    public String toString() {
        return "LLamado [id=" + ID + ", clave=" + clave + ", sector=" + sector + ", direccion=" + direccion + ", unidades=" + unidades + ", date=" + date.getTime()
                + ", iconResourceID=" + iconResourceID + "]";
    }
}
