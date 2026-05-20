-- Chạy nếu bảng majors đã tạo với created_by/updated_by/deleted_by kiểu UNIQUEIDENTIFIER
USE schoolmanager;
GO

IF EXISTS (
    SELECT 1 FROM sys.columns c
    JOIN sys.types t ON c.user_type_id = t.user_type_id
    WHERE c.object_id = OBJECT_ID(N'dbo.majors')
      AND c.name = N'created_by'
      AND t.name = N'uniqueidentifier'
)
BEGIN
    ALTER TABLE dbo.majors ALTER COLUMN created_by NVARCHAR(255) NULL;
    ALTER TABLE dbo.majors ALTER COLUMN updated_by NVARCHAR(255) NULL;
    ALTER TABLE dbo.majors ALTER COLUMN deleted_by NVARCHAR(255) NULL;
END
GO
