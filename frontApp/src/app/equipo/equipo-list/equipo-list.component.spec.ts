import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EquipoListComponent } from './equipo-list.component';
import { EquipoService } from '../equipo.service';
import { of } from 'rxjs';
import { EquipoDetail } from '../equipo-detail';

describe('EquipoListComponent', () => {
  let component: EquipoListComponent;
  let fixture: ComponentFixture<EquipoListComponent>;
  let equipoService: EquipoService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EquipoListComponent],
      providers: [EquipoService]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EquipoListComponent);
    component = fixture.componentInstance;
    equipoService = TestBed.inject(EquipoService);
    fixture.detectChanges();
  });
  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getEquipos method and populate equipos array', () => {
    const equipos: EquipoDetail[] = [
      {
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
      },
      {
        id: 2,
        nombre: 'Equipo 2',
        paisProcedencia: 'País 2',
        banderaPais: 'bandera2.png',
        logo: 'logo2.png',
        torneosParticipados: [],
        competenciasParticipadas: [],
        competenciasGanadas: [],
        resultadosEnCompetencias: [],
        integrantes: []
      }
    ];
    spyOn(equipoService, 'getEquipos').and.returnValue(of(equipos));

    component.ngOnInit();

    expect(equipoService.getEquipos).toHaveBeenCalled();
    expect(component.equipos).toEqual(equipos);
  });

  it('should set selectedEquipo and selected to true when onSelected method is called', () => {
    const equipo: EquipoDetail = {
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

    component.onSelected(equipo);

    expect(component.selectedEquipo).toEqual(equipo);
    expect(component.selected).toBeTrue();
  });

  it('should filter equipos array based on searchQuery when filterEquipos method is called', () => {
    const equipos: EquipoDetail[] = [
      {
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
      },
      {
        id: 2,
        nombre: 'Equipo 2',
        paisProcedencia: 'País 2',
        banderaPais: 'bandera2.png',
        logo: 'logo2.png',
        torneosParticipados: [],
        competenciasParticipadas: [],
        competenciasGanadas: [],
        resultadosEnCompetencias: [],
        integrantes: []
      },
      {
        id: 3,
        nombre: 'Equipo 3',
        paisProcedencia: 'País 3',
        banderaPais: 'bandera3.png',
        logo: 'logo3.png',
        torneosParticipados: [],
        competenciasParticipadas: [],
        competenciasGanadas: [],
        resultadosEnCompetencias: [],
        integrantes: []
      }
    ];
    component.equipos = equipos;
    component.searchQuery = 'equipo 2';

    component.filterEquipos();

    expect(component.equipos.length).toBe(1);
    expect(component.equipos[0]).toEqual(equipos[1]);
  });
  */
});
