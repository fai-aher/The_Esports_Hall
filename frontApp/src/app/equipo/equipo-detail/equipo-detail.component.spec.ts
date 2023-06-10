import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EquipoDetailComponent } from './equipo-detail.component';
import { ActivatedRoute } from '@angular/router';
import { EquipoService } from '../equipo.service';
import { of } from 'rxjs';
import { EquipoDetail } from '../equipo-detail';

describe('EquipoDetailComponent', () => {
  let component: EquipoDetailComponent;
  let fixture: ComponentFixture<EquipoDetailComponent>;
  let mockActivatedRoute: any;
  let mockEquipoService: any;

  beforeEach(async () => {
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: () => '1'
        }
      }
    };

    mockEquipoService = jasmine.createSpyObj('EquipoService', ['getEquipo']);
    mockEquipoService.getEquipo.and.returnValue(of({} as EquipoDetail)); // Assuming empty equipoDetail for testing

    await TestBed.configureTestingModule({
      declarations: [EquipoDetailComponent],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: EquipoService, useValue: mockEquipoService }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EquipoDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getEquipo method when equipoId is provided', () => {
    expect(mockEquipoService.getEquipo).toHaveBeenCalled();
  });

  it('should display equipoDetail data', () => {
    const equipoDetail: EquipoDetail = {
      id: 1,
      nombre: 'Equipo 1',
      paisProcedencia: 'País 1',
      banderaPais: 'bandera1.png',
      logo: 'logo1.png',
      torneosParticipados: [],
      competenciasParticipadas: [],
      competenciasGanadas: [],
      resultadosEnCompetencias: [],
      integrantes: []
    };
    component.equipoDetail = equipoDetail;
    fixture.detectChanges();

    const nombreElement = fixture.nativeElement.querySelector('.h3');
    expect(nombreElement.textContent).toContain(equipoDetail.nombre);

    const paisElement = fixture.nativeElement.querySelector('dd:nth-of-type(1)');
    expect(paisElement.textContent).toContain(equipoDetail.paisProcedencia);

    const banderaPaisElement = fixture.nativeElement.querySelector('.img-fluid-2');
    expect(banderaPaisElement.src).toContain(equipoDetail.banderaPais);
  });

});
