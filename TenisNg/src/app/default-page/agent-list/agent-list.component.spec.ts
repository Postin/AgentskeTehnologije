import { AgentListComponent } from "./agent-list.component";
import { TestBed, ComponentFixture, async } from '@angular/core/testing';

describe('DefaultPageComponent', () => {
    let component: AgentListComponent;
    let fixture: ComponentFixture<AgentListComponent>;
  
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [ AgentListComponent ]
      })
      .compileComponents();
    }));
  
    beforeEach(() => {
      fixture = TestBed.createComponent(AgentListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });