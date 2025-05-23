package idv.ex.cs.hogwartsartifactsonline.artifact;

import idv.ex.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import idv.ex.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId) {
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(idWorker.nextId() + "");
        return artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact artifact) {
        return  this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(artifact.getName());
                    oldArtifact.setDescription(artifact.getDescription());
                    oldArtifact.setImageUrl(artifact.getImageUrl());
                    return artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    public void delete(String artifactId) {
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
        this.artifactRepository.deleteById(artifactId);
    }
}
