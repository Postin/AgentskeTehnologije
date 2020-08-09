import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformativesListComponent } from './performatives-list.component';

describe('PerformativesListComponent', () => {
  let component: PerformativesListComponent;
  let fixture: ComponentFixture<PerformativesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PerformativesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformativesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
