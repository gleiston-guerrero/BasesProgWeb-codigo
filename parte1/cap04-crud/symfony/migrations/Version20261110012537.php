<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20261110012537 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE proveedores (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ruc VARCHAR(13) NOT NULL, razon_social VARCHAR(200) NOT NULL, limite_credito NUMERIC(12, 2) NOT NULL)');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_864FA8F12EC7D87D ON proveedores (ruc)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE proveedores');
    }
}
