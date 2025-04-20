package idv.ex.cs.hogwartsartifactsonline.artifact.converter;

import idv.ex.cs.hogwartsartifactsonline.artifact.Artifact;
import idv.ex.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import idv.ex.cs.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    private WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(),
                                                    source.getName(),
                                                    source.getDescription(),
                                                    source.getImageUrl(),
                                                    source.getOwner() != null
                                                            ? wizardToWizardDtoConverter.convert(source.getOwner())
                                                            : null);
        return artifactDto;
    }

}
