import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CompetenciaDetail } from '../competencia-detail';
import { CompetenciaService } from '../competencia.service';

@Component({
  selector: 'app-competencia-detail',
  templateUrl: './competencia-detail.component.html',
  styleUrls: ['./competencia-detail.component.css']
})
export class CompetenciaDetailComponent implements OnInit {

  competenciaId!: string;
  @Input() competenciaDetail!: CompetenciaDetail;

  constructor(
    private route: ActivatedRoute,
    private competenciaService: CompetenciaService) { }

  getCompetencia(){
    this.competenciaService.getCompetencia(this.competenciaId).subscribe(competencia=>{
      this.competenciaDetail = competencia;
    })
  }

  ngOnInit() {
    if(this.competenciaDetail === undefined){
      this.competenciaId = this.route.snapshot.paramMap.get('id')!
      if(this.competenciaId){
        this.getCompetencia();
      }
    }
  }

}
