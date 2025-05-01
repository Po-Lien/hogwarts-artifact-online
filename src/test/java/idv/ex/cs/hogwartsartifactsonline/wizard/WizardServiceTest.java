package idv.ex.cs.hogwartsartifactsonline.wizard;

import idv.ex.cs.hogwartsartifactsonline.artifact.Artifact;
import idv.ex.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import idv.ex.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        this.wizards = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindByIdSuccess() {
        //Given
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");
        wizard.addArtifact(a1);
        wizard.addArtifact(a3);
        given(this.wizardRepository.findById(wizard.getId())).willReturn(Optional.of(wizard));

        //When
        Wizard returnWizard = this.wizardService.findById(wizard.getId());

        //Then
        assertThat(returnWizard.getId()).isEqualTo(wizard.getId());
        assertThat(returnWizard.getName()).isEqualTo(wizard.getName());
        assertThat(returnWizard.getArtifacts()).isEqualTo(wizard.getArtifacts());
        verify(this.wizardRepository, times(1)).findById(wizard.getId());
    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(this.wizardRepository.findById(anyInt())).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(() -> {
            this.wizardService.findById(1);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 1 :(");
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        //When
        List<Wizard> actualWizards = this.wizardService.findAll();

        //Then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(this.wizardRepository, times(1)).findAll();

    }

    @Test
    void testSaveSuccess() {
        //Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Wizard 4");
        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        //When
        Wizard savedWizard = this.wizardService.save(newWizard);

        //Then
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        verify(this.wizardRepository, times(1)).save(newWizard);

    }

    @Test
    void testUpdateSuccess() {
        //Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(2);
        oldWizard.setName("Harry Potter");

        Wizard update = new Wizard();
        update.setId(2);
        update.setName("Harry Potter Updated");

        given(this.wizardRepository.findById(oldWizard.getId())).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //When
        Wizard updatedWizard = this.wizardService.update(2, update);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(this.wizardRepository, times(1)).findById(2);
        verify(this.wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        //Given
        Wizard update = new Wizard();
        update.setId(2);
        update.setName("Harry Potter Updated");
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(update.getId(), update);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(update.getId());

    }

    @Test
    void testDeleteSuccess() {
        //Given
        Wizard deletedWizard = new Wizard();
        deletedWizard.setId(2);
        deletedWizard.setName("Harry Potter");
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(deletedWizard));
        doNothing().when(this.wizardRepository).deleteById(2);

        //When
        this.wizardService.delete(2);

        //Then
        verify(this.wizardRepository, times(1)).deleteById(2);

    }

    @Test
    void testDeleteNotFound() {
        //Given
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.delete(2);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(2);
    }

    @Test
    void testAssignArtifactSuccess() {
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));

        //When
        this.wizardService.assignArtifact(3, "1250808601744904192");

        //Then
        assertThat(a.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifacts()).contains(a);

    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        //Given
        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "1250808601744904192");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");

    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "1250808601744904192");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 3 :(");
        assertThat(a.getOwner().getId()).isEqualTo(2);

    }

}
