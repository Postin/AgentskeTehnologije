import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentClassesComponent } from './agent-classes.component';

describe('AgentClassesComponent', () => {
  let component: AgentClassesComponent;
  let fixture: ComponentFixture<AgentClassesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgentClassesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentClassesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
