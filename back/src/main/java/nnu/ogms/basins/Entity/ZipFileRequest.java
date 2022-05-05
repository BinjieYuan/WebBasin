package nnu.ogms.basins.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZipFileRequest {
    private String realUrl;
    private String fileName;
}
