import { Component, Input, OnInit } from '@angular/core';
import { EquipoDetail } from '../equipo-detail';
import { ActivatedRoute } from '@angular/router';
import { EquipoService } from '../equipo.service';

@Component({
  selector: 'app-equipo-detail',
  templateUrl: './equipo-detail.component.html',
  styleUrls: ['./equipo-detail.component.css']
})
export class EquipoDetailComponent implements OnInit {

  equipoId!: string;
  @Input() equipoDetail!: EquipoDetail;

  constructor(
    private route: ActivatedRoute,
    private equipoService: EquipoService
   ) {}

   //Get an author in the routing
   getEquipo() {
    this.equipoService.getEquipo(this.equipoId).subscribe(equipo=>{
      this.equipoDetail = equipo;
    });
  }

  ngOnInit() {
    if(this.equipoDetail === undefined){
      this.equipoId = this.route.snapshot.paramMap.get('id')!
      if(this.equipoId) {
        this.getEquipo();
      }
    }
  }

}
