package com.example.select_db;

import android.os.Parcel;
import android.os.Parcelable;

public class Squadra implements Parcelable {
    public int id;
    public String nome;
    public String citta;
    public String paese;

    public Squadra(int id, String nome, String citta, String paese) {
        this.id = id;
        this.nome = nome;
        this.citta = citta;
        this.paese = paese;
    }

    protected Squadra(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        citta = in.readString();
        paese = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(citta);
        dest.writeString(paese);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Squadra> CREATOR = new Creator<Squadra>() {
        @Override
        public Squadra createFromParcel(Parcel in) {
            return new Squadra(in);
        }

        @Override
        public Squadra[] newArray(int size) {
            return new Squadra[size];
        }
    };

    @Override
    public String toString() {
        return "Squadra{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", citta='" + citta + '\'' +
                ", paese='" + paese + '\'' +
                '}';
    }
}
