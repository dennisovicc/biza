import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreditoAprovacaoComponent } from './credito-aprovacao.component';

describe('CreditoAprovacaoComponent', () => {
  let component: CreditoAprovacaoComponent;
  let fixture: ComponentFixture<CreditoAprovacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreditoAprovacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreditoAprovacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
