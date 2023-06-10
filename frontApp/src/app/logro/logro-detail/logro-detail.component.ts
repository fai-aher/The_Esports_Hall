import { Component, Input, OnInit } from '@angular/core';
import { LogroDetail } from '../logro-detail';
import { ActivatedRoute } from '@angular/router';
import { LogroService } from '../logro.service';

@Component({
  selector: 'app-logro-detail',
  templateUrl: './logro-detail.component.html',
  styleUrls: ['./logro-detail.component.css']
})
export class LogroDetailComponent implements OnInit {

  logroId!: string;
  @Input() logroDetail!: LogroDetail;

  constructor(
    private route: ActivatedRoute,
    private logroService: LogroService) { }

  getLogro() {
    this.logroService.getLogro(this.logroId).subscribe(
      (logroDetail: LogroDetail) => {
        this.logroDetail = logroDetail;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  ngOnInit() {
    if (!this.logroDetail) {
      this.logroId = this.route.snapshot.paramMap.get('id')!;
      if (this.logroId) {
        this.getLogro();
      }
    }
  }
}
