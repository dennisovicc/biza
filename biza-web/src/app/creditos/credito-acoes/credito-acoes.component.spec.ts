import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreditoAcoesComponent } from './credito-acoes.component';

describe('CreditoAccoesComponent', () => {
  let component: CreditoAcoesComponent;
  let fixture: ComponentFixture<CreditoAcoesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreditoAcoesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreditoAcoesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
