import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TorneoListComponent } from './torneo-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TorneoListComponent', () => {
  let component: TorneoListComponent;
  let fixture: ComponentFixture<TorneoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TorneoListComponent],
    }).compileComponents();
  });

  /*

  beforeEach(() => {
    fixture = TestBed.createComponent(TorneoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('onSelected', () => {
    it('should set selected to true and selectedTorneo to the provided torneo', () => {
      const torneo: TorneoDetail = {
        id: 1,
        nombreTorneo: 'Torneo 1',
        fechaFinalizacion: '2023-01-01',
        paisRealizacion: 'País 1',
        imagenRepresentativa: 'imagen1.png',
        enlacePaginaWeb: 'www.torneo1.com',
        organizador: 'Organizador 1',
        videojuego: 'Videojuego 1',
        equiposParticipantes: [],
        competencias: []
      };

      component.onSelected(torneo);

      expect(component.selected).toBe(true);
      expect(component.selectedTorneo).toEqual(torneo);
    });
  });

  describe('ordenarTorneos', () => {
    it('should sort the torneos array by nombreTorneo in ascending order', () => {
      const torneos: TorneoDetail[] = [
        {
          id: 1,
          nombreTorneo: 'Torneo 1',
          fechaFinalizacion: '2023-01-01',
          paisRealizacion: 'País 1',
          imagenRepresentativa: 'imagen1.png',
          enlacePaginaWeb: 'www.torneo1.com',
          organizador: 'Organizador 1',
          videojuego: 'Videojuego 1',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 3,
          nombreTorneo: 'Torneo 3',
          fechaFinalizacion: '2023-03-03',
          paisRealizacion: 'País 3',
          imagenRepresentativa: 'imagen3.png',
          enlacePaginaWeb: 'www.torneo3.com',
          organizador: 'Organizador 3',
          videojuego: 'Videojuego 3',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 2,
          nombreTorneo: 'Torneo 2',
          fechaFinalizacion: '2023-02-02',
          paisRealizacion: 'País 2',
          imagenRepresentativa: 'imagen2.png',
          enlacePaginaWeb: 'www.torneo2.com',
          organizador: 'Organizador 2',
          videojuego: 'Videojuego 2',
          equiposParticipantes: [],
          competencias: []
        },
      ];
      component.torneos = torneos;

      component.ordenarTorneos();

      expect(component.torneos).toEqual([
        {
          id: 1,
          nombreTorneo: 'Torneo 1',
          fechaFinalizacion: '2023-01-01',
          paisRealizacion: 'País 1',
          imagenRepresentativa: 'imagen1.png',
          enlacePaginaWeb: 'www.torneo1.com',
          organizador: 'Organizador 1',
          videojuego: 'Videojuego 1',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 2,
          nombreTorneo: 'Torneo 2',
          fechaFinalizacion: '2023-02-02',
          paisRealizacion: 'País 2',
          imagenRepresentativa: 'imagen2.png',
          enlacePaginaWeb: 'www.torneo2.com',
          organizador: 'Organizador 2',
          videojuego: 'Videojuego 2',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 3,
          nombreTorneo: 'Torneo 3',
          fechaFinalizacion: '2023-03-03',
          paisRealizacion: 'País 3',
          imagenRepresentativa: 'imagen3.png',
          enlacePaginaWeb: 'www.torneo3.com',
          organizador: 'Organizador 3',
          videojuego: 'Videojuego 3',
          equiposParticipantes: [],
          competencias: []
        },
      ]);
    });
  });

  describe('filterTorneos', () => {
    beforeEach(() => {
      component.torneos = [
        {
          id: 1,
          nombreTorneo: 'Torneo 1',
          fechaFinalizacion: '2023-01-01',
          paisRealizacion: 'País 1',
          imagenRepresentativa: 'imagen1.png',
          enlacePaginaWeb: 'www.torneo1.com',
          organizador: 'Organizador 1',
          videojuego: 'Videojuego 1',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 2,
          nombreTorneo: 'Torneo 2',
          fechaFinalizacion: '2023-02-02',
          paisRealizacion: 'País 2',
          imagenRepresentativa: 'imagen2.png',
          enlacePaginaWeb: 'www.torneo2.com',
          organizador: 'Organizador 2',
          videojuego: 'Videojuego 2',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 3,
          nombreTorneo: 'Torneo 3',
          fechaFinalizacion: '2023-03-03',
          paisRealizacion: 'País 3',
          imagenRepresentativa: 'imagen3.png',
          enlacePaginaWeb: 'www.torneo3.com',
          organizador: 'Organizador 3',
          videojuego: 'Videojuego 3',
          equiposParticipantes: [],
          competencias: []
        },
      ];
    });

    it('should filter torneos based on selectedFilter and searchTerm', () => {
      component.selectedFilter = 'nombre';
      component.searchTerm = 'Torneo 1';

      component.filterTorneos();

      expect(component.torneos).toEqual([
        {
          id: 1,
          nombreTorneo: 'Torneo 1',
          fechaFinalizacion: '2023-01-01',
          paisRealizacion: 'País 1',
          imagenRepresentativa: 'imagen1.png',
          enlacePaginaWeb: 'www.torneo1.com',
          organizador: 'Organizador 1',
          videojuego: 'Videojuego 1',
          equiposParticipantes: [],
          competencias: []
        },
      ]);
    });

    it('should reset torneos when searchTerm is empty', () => {
      component.selectedFilter = 'nombre';
      component.searchTerm = '';

      component.filterTorneos();

      expect(component.torneos).toEqual([
        {
          id: 1,
          nombreTorneo: 'Torneo 1',
          fechaFinalizacion: '2023-01-01',
          paisRealizacion: 'País 1',
          imagenRepresentativa: 'imagen1.png',
          enlacePaginaWeb: 'www.torneo1.com',
          organizador: 'Organizador 1',
          videojuego: 'Videojuego 1',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 2,
          nombreTorneo: 'Torneo 2',
          fechaFinalizacion: '2023-02-02',
          paisRealizacion: 'País 2',
          imagenRepresentativa: 'imagen2.png',
          enlacePaginaWeb: 'www.torneo2.com',
          organizador: 'Organizador 2',
          videojuego: 'Videojuego 2',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 3,
          nombreTorneo: 'Torneo 3',
          fechaFinalizacion: '2023-03-03',
          paisRealizacion: 'País 3',
          imagenRepresentativa: 'imagen3.png',
          enlacePaginaWeb: 'www.torneo3.com',
          organizador: 'Organizador 3',
          videojuego: 'Videojuego 3',
          equiposParticipantes: [],
          competencias: []
        },
      ]);
    });

    it('should show all torneos when selectedFilter is invalid', () => {
      component.selectedFilter = 'invalid';
      component.searchTerm = 'Torneo';

      component.filterTorneos();

      expect(component.torneos).toEqual([
        {
          id: 1,
          nombreTorneo: 'Torneo 1',
          fechaFinalizacion: '2023-01-01',
          paisRealizacion: 'País 1',
          imagenRepresentativa: 'imagen1.png',
          enlacePaginaWeb: 'www.torneo1.com',
          organizador: 'Organizador 1',
          videojuego: 'Videojuego 1',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 2,
          nombreTorneo: 'Torneo 2',
          fechaFinalizacion: '2023-02-02',
          paisRealizacion: 'País 2',
          imagenRepresentativa: 'imagen2.png',
          enlacePaginaWeb: 'www.torneo2.com',
          organizador: 'Organizador 2',
          videojuego: 'Videojuego 2',
          equiposParticipantes: [],
          competencias: []
        },
        {
          id: 3,
          nombreTorneo: 'Torneo 3',
          fechaFinalizacion: '2023-03-03',
          paisRealizacion: 'País 3',
          imagenRepresentativa: 'imagen3.png',
          enlacePaginaWeb: 'www.torneo3.com',
          organizador: 'Organizador 3',
          videojuego: 'Videojuego 3',
          equiposParticipantes: [],
          competencias: []
        },
      ]);
    });
  });

  describe('resetTorneos', () => {
    it('should call getTorneos method', () => {
      spyOn(component, 'getTorneos');

      component.resetTorneos();

      expect(component.getTorneos).toHaveBeenCalled();
    });
  });

  */
});
