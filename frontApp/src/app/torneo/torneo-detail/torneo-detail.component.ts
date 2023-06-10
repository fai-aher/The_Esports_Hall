import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TorneoDetail } from '../torneo-detail';
import { TorneoService } from '../torneo.service';

@Component({
  selector: 'app-torneo-detail',
  templateUrl: './torneo-detail.component.html',
  styleUrls: ['./torneo-detail.component.css']
})
export class TorneoDetailComponent implements OnInit {

  torneoId!: string;
  @Input() torneoDetail!: TorneoDetail;

  constructor(
    private route: ActivatedRoute,
    private torneoService: TorneoService) { }

  getTorneo(){
    this.torneoService.getTorneo(this.torneoId).subscribe(torneo=>{
      this.torneoDetail = torneo;
    })
  }

  ngOnInit() {
    if(this.torneoDetail === undefined){
      this.torneoId = this.route.snapshot.paramMap.get('id')!
      if(this.torneoId){
        this.getTorneo();
      }
    }
  }

}
