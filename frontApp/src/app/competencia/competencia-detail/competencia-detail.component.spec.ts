/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CompetenciaDetailComponent } from './competencia-detail.component';
import { faker } from '@faker-js/faker';
import { CompetenciaDetail } from '../competencia-detail';
import { Competencia } from '../competencia';

describe('CompetenciaDetailComponent', () => {
  let component: CompetenciaDetailComponent;
  let fixture: ComponentFixture<CompetenciaDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompetenciaDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompetenciaDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
