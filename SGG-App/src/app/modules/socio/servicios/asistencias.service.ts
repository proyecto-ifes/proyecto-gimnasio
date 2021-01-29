import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AsistenciasService {
  ip = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }
  
  httpOptions = {
    headers: new HttpHeaders({
      'Accept':  'application/json;profile=urn:org.apache.isis/v1',
      'Authorization': 'Basic aXNpcy1tb2R1bGUtc2VjdXJpdHktYWRtaW46cGFzcw==',
    })
  }

  private Url = this.ip+'/restful/objects/gimnasio.socios/';

  getAsistencias(id: number){
     return this.httpClient.get(this.Url+id+'/collections/asistenciaSocio', this.httpOptions);
  }


}
